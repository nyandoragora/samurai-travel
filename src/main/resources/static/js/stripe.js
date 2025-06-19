/**
 * 
 */


const stripe = Stripe('pk_test_51RbfdCIhpOfYfENirRKVOapoHZj3kTfrNG1obEFzEMCfOx12pPMOJotgRLuVJKqdl5kAnPFycKkfsXGibWAWfF3a003kjyI6jY');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click' , () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});

